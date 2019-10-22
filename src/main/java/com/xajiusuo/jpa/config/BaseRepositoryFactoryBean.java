package com.xajiusuo.jpa.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by konghao on 2016/12/7.
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class BaseRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

    public BaseRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new BaseRepositoryFactory(em);
    }

    //创建一个内部类，该类不用在外部访问
    private static class BaseRepositoryFactory<T, I extends Serializable>  extends JpaRepositoryFactory {

        private final EntityManager em;

        public BaseRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }

//        设置具体的实现类是BaseRepositoryImple
//        @Override
//        protected Object getTargetRepository(RepositoryInformation information) {
//            return new BaseDaoImpl<T, I>((Class<T>) information.getDomainType(), em);
//        }

        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
            JpaEntityInformation entityInformation = this.getEntityInformation(information.getDomainType());
//            Object repository = this.getTargetRepositoryViaReflection(information, new Object[]{entityInformation, entityManager});
            BaseDaoImpl repository = new BaseDaoImpl<T, I>((Class<T>) information.getDomainType(), entityManager);
            repository.setEntityInformation(entityInformation);
            Assert.isInstanceOf(JpaRepositoryImplementation.class, repository);
            return (JpaRepositoryImplementation)repository;
        }

        //设置具体的实现类的class
        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseDaoImpl.class;
        }
    }
}