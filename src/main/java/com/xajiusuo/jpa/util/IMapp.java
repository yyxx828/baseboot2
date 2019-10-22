package com.xajiusuo.jpa.util;

import java.io.PrintStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IMapp extends Serializable {
	/**
	 * @see 查看定义常量
	 * */
	public final static int show_const_declare = 3;

	/**
	 * @see 查看父类变量
	 * */
	public final static int show_const_parend = 4;

	/**
	 * @see 查看定义变量
	 * */
	public final static int show_field_declare = 1;

	/**
	 * @see 查看父级变量
	 * */
	public final static int show_field_parend = 2;

	/**
	 * @see 查看定义方法
	 * */
	public final static int show_method_declare = 5;

	/**
	 * @see 查看父类方法
	 * */
	public final static int show_method_parend = 6;

	/**
	 * @param entity 加载实体映射对象
	 * */
	public IMapp load(Object entity);

	/**
	 * @see 重新加载
	 * */
	public void reload(Object entity);
	
	/**
	 * @see 方法运行
	 * @param MethodName 方法名称
	 * @param args 该方法对应参数
	 * */
	public Object invoke(String methodName, Object... args);

	/**
	 * @see 卸载映射实体
	 * */
	public void unLoad();

	/**
	 * @see 获得实体树形结构
	 * */
	public void getClassInfomation();

	/**
	 * @see 获得父类映射实体
	 * */
	public IMapp getParentIMapp();

	/**
	 * @param fieldName 包涵变量名
	 * */
	public boolean containsField(String fieldName);

	public boolean containsConstant(String fieldName);

	/**
	 * @param methodName 包涵方法名
	 * */
	public boolean containsMethod(String methodName);

	/**
	 * @see 获得该变量的定义声明
	 * */
	public String getFieldType(String fieldName);

	/**
	 * @see 获得该类序列化版本若不存在返回null
	 * */
	public Long getVersion();

	/**
	 * @see 获得变量地址引用
	 * */
	public Object getObjectbyField(String fieldName);

	/**
	 * @see 获得final常量属性
	 * */
	public Map<String, Object> getProperties();

	public String getSimpleName(String fieldName);

	public boolean isDate(String fieldName);

	/**
	 * @see 获得fieldName对应属性
	 * */
	public Field getField(String fieldName);

	/**
	 * @see 获得所有变量名称
	 * */
	public Set<String> getFields();

	public Class<?> getFieldInterface(String field, int... num);

	public Class<?> getFieldSuper(String field, int grade, int... num);

	/**
	 * @see 获得所有方法名称
	 * */
	public Collection<Method> getMethods();

	/**
	 * @see 获得methodName对应的方法
	 * */
	public Method getMethod(String methodName);

	/**
	 * @param allinfor 是否展示详细信息
	 * @see 展示所有属性和方法
	 * */
	public void showAllBody(boolean... allinfor);

	/**
	 * @see 通过参数查看对性属性和方法
	 * */
	public void showByParm(int... value);

	/**
	 * @see 设置展示平台
	 * */
	public void setout(PrintStream out);

	public void setControl(boolean isControl);

	/**
	 * @see 索取fieldName对应类型默认value为该类型进行赋值
	 * */
	public void setObject(String fieldName, Object value);
	
	/**
	 * @see 索取fieldName对应类型默认value为该类型进行赋值
	 * */
	public void setObjectIgnore(String fieldName, Object value);
	
	public void setValue(Map<String, Object> map);
	
	public void setValueIgnore(Map<String, Object> map);

	public <T> T getEntity();
	
	public <T> T getNewEntity();

	public void setObjectbyString(String fieldName, String value);

	public Boolean getBoolean(String fieldName);

	public Byte getByte(String fieldName);

	public Character getChar(String fieldName);

	public Double getDouble(String fieldName);

	public Float getFloat(String fieldName);

	public Integer getInt(String fieldName);

	public Long getLong(String fieldName);

	public Short getShort(String fieldName);

	public String getString(String fieldName);

	public Boolean[] getBooleanArray(String fieldName);

	public Byte[] getByteArray(String fieldName);

	public Character[] getCharArray(String fieldName);

	public Double[] getDoubleArray(String fieldName);

	public Float[] getFloatArray(String fieldName);

	public Integer[] getIntArray(String fieldName);

	public Long[] getLongArray(String fieldName);

	public Short[] getShortArray(String fieldName);

	public boolean[] getbooleanArray(String fieldName);

	public byte[] getbyteArray(String fieldName);

	public char[] getcharArray(String fieldName);

	public double[] getdoubleArray(String fieldName);

	public float[] getfloatArray(String fieldName);

	public int[] getintArray(String fieldName);

	public long[] getlongArray(String fieldName);

	public short[] getshortArray(String fieldName);

	public String[] getStringArray(String fieldName);

	public void setBoolean(String fieldName, Boolean value);

	public void setByte(String fieldName, Byte value);

	public void setChar(String fieldName, Character value);

	public void setDouble(String fieldName, Double value);

	public void setFloat(String fieldName, Float value);

	public void setInt(String fieldName, Integer value);

	public void setLong(String fieldName, Long value);

	public void setShort(String fieldName, Short value);

	public void setString(String fieldName, String value);

	public void setBooleanArray(String fieldName, Boolean[] value);

	public void setByteArray(String fieldName, Byte[] value);

	public void setCharArray(String fieldName, Character[] value);

	public void setDoubleArray(String fieldName, Double[] value);

	public void setFloatArray(String fieldName, Float[] value);

	public void setIntArray(String fieldName, Integer[] value);

	public void setLongArray(String fieldName, Long[] value);

	public void setShortArray(String fieldName, Short[] value);

	public void setStringArray(String fieldName, String[] value);

	public Class<?> getEntityClass();

	public Object getField0(String type, Object value);

	public Method getProperByAnnotation(Class<? extends Annotation> annotationClass);

	public List<Method> getPropersByAnnotation(Class<? extends Annotation> annotationClass);

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass);

	public <A extends Annotation> A getMethodAnnotation(Class<A> annotationClass, String name);

	public <A extends Annotation> A getMethodAnnotationAddGet(Class<A> annotationClass, String name);

	public void invokeSetMethod(String methodName, Object value);

	public <T> T getEntityByClass(T t);

}