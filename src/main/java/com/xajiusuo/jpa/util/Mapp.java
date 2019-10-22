package com.xajiusuo.jpa.util;

import java.lang.reflect.Type;
import java.util.Set;

public final class Mapp extends AbstractMapp implements IMapp {
	private static final long serialVersionUID = 6444753389718940705L;

	private Mapp() {
	}
	
	public static void copyNulProperties(Object o, Object target) {
		if(o == null || target == null){
			return;
		}
		IMapp m1 = Mapp.createMapp(o);
		IMapp m2 = Mapp.createMapp(target);
		Set<String> ss =  m2.getFields();
		for(String f:ss){
			if(m2.getObjectbyField(f) == null){
				m2.setObject(f, m1.getObjectbyField(f));
			}
		}
	}
	
	public static void copyProperties(Object o, Object target) {
		if(o == null || target == null){
			return;
		}
		IMapp m1 = Mapp.createMapp(o);
		IMapp m2 = Mapp.createMapp(target);
		Set<String> ss =  m2.getFields();
		for(String f:ss){
			m2.setObject(f, m1.getObjectbyField(f));
		}
	}
	
	public static IMapp createMapp() {
		Mapp mapp = new Mapp();
		return mapp;
	}

	public static IMapp createMapp(Object entity) {
		return createMapp(entity, true);
	}

	public static IMapp createMapp(Object entity, boolean isControl) {
		IMapp mapp = new Mapp();
		mapp.setControl(isControl);
		mapp.load(entity);
		return mapp;
	}

	public void getClassInfomation() {
		System.out.println("\n类[" + entityclass.getSimpleName() + "]信息：" + entityclass.toString());
		Type gen = entityclass.getGenericSuperclass();
		System.out.println("父类：\t" + (gen == null ? "顶级类" : gen));
		Type[] gens = entityclass.getGenericInterfaces();
		System.out.println("接口：\t" + gens.length + " 个");
		for(Type t : gens)
			System.out.println("\t" + t + "\n");
		System.out.println("自定义常量属性个数：" + map_ds.size());
		System.out.println("自定义变量属性个数：" + map_df.size());
		System.out.println("自定义方法个数：" + map_dm.size());
		System.out.println("======================================\n");

		if(superMapp != null) {
			System.out.println("类 [" + entityclass.getSimpleName() + "] 父类["
					+ entityclass.getSuperclass().getSimpleName() + "]信息：");
			superMapp.getClassInfomation();
		}
	}

	public IMapp getParentIMapp() {
		return super.getParentIMapp();
	}

	public Mapp GetSuperMapp() {
		return (Mapp)(super.getParentIMapp());
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntity() {
		return (T)entity;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getNewEntity(){
		Object r = entity;
		try {
			entity = entity.getClass().newInstance();
		}catch(Exception e) {
		}
		return (T)r;
	}
	
	public Boolean getBoolean(String fieldName) {
		try {
			return Boolean.valueOf(getObjectbyField(fieldName) + "");
		}catch(Exception e) {
			return null;
		}
	}

	public Boolean[] getBooleanArray(String fieldName) {
		try {
			return (Boolean[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public Byte getByte(String fieldName) {
		try {
			return (Byte)getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public Byte[] getByteArray(String fieldName) {
		try {
			return (Byte[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public Character getChar(String fieldName) {
		try {
			return (Character)getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public Character[] getCharArray(String fieldName) {
		try {
			return (Character[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public Double getDouble(String fieldName) {
		try {
			return Double.valueOf(getObjectbyField(fieldName) + "");
		}catch(Exception e) {
			return null;
		}
	}

	public Double[] getDoubleArray(String fieldName) {
		try {
			return (Double[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public Float getFloat(String fieldName) {
		try {
			return Float.valueOf(getObjectbyField(fieldName) + "");
		}catch(Exception e) {
			return null;
		}
	}

	public Float[] getFloatArray(String fieldName) {
		try {
			return (Float[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public Integer getInt(String fieldName) {
		try {
			return Integer.valueOf(getObjectbyField(fieldName) + "");
		}catch(Exception e) {
			return null;
		}
	}

	public Integer[] getIntArray(String fieldName) {
		try {
			return (Integer[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public Long getLong(String fieldName) {
		try {
			return Long.valueOf(getObjectbyField(fieldName) + "");
		}catch(Exception e) {
			return null;
		}
	}

	public Long[] getLongArray(String fieldName) {
		try {
			return (Long[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public Short getShort(String fieldName) {
		try {
			return Short.valueOf(getObjectbyField(fieldName) + "");
		}catch(Exception e) {
			return null;
		}
	}

	public Short[] getShortArray(String fieldName) {
		try {
			return (Short[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public String getString(String fieldName) {
		try {
			return getObjectbyField(fieldName) + "";
		}catch(Exception e) {
			return null;
		}
	}

	public String[] getStringArray(String fieldName) {
		try {
			return (String[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public boolean[] getbooleanArray(String fieldName) {
		try {
			return (boolean[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public byte[] getbyteArray(String fieldName) {
		try {
			return (byte[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public char[] getcharArray(String fieldName) {
		try {
			return (char[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public double[] getdoubleArray(String fieldName) {
		try {
			return (double[])getObjectbyField(fieldName);
		}catch(Exception e) {
			return null;
		}
	}

	public float[] getfloatArray(String fieldName) {
		return (float[])getObjectbyField(fieldName);
	}

	public int[] getintArray(String fieldName) {
		return (int[])getObjectbyField(fieldName);
	}

	public long[] getlongArray(String fieldName) {
		return (long[])getObjectbyField(fieldName);
	}

	public short[] getshortArray(String fieldName) {
		return (short[])getObjectbyField(fieldName);
	}

	public void setBoolean(String fieldName, Boolean value) {
		setObject(fieldName, value);
	}

	public void setBooleanArray(String fieldName, Boolean[] value) {
		setObject(fieldName, value);
	}

	public void setByte(String fieldName, Byte value) {
		setObject(fieldName, value);
	}

	public void setByteArray(String fieldName, Byte[] value) {
		setObject(fieldName, value);
	}

	public void setChar(String fieldName, Character value) {
		setObject(fieldName, value);
	}

	public void setCharArray(String fieldName, Character[] value) {
		setObject(fieldName, value);
	}

	public void setDouble(String fieldName, Double value) {
		setObject(fieldName, value);
	}

	public void setDoubleArray(String fieldName, Double[] value) {
		setObject(fieldName, value);
	}

	public void setFloat(String fieldName, Float value) {
		setObject(fieldName, value);
	}

	public void setFloatArray(String fieldName, Float[] value) {
		setObject(fieldName, value);
	}

	public void setInt(String fieldName, Integer value) {
		setObject(fieldName, value);
	}

	public void setIntArray(String fieldName, Integer[] value) {
		setObject(fieldName, value);
	}

	public void setLong(String fieldName, Long value) {
		setObject(fieldName, value);
	}

	public void setLongArray(String fieldName, Long[] value) {
		setObject(fieldName, value);
	}

	public void setShort(String fieldName, Short value) {
		setObject(fieldName, value);
	}

	public void setShortArray(String fieldName, Short[] value) {
		setObject(fieldName, value);
	}

	public void setString(String fieldName, String value) {
		setObject(fieldName, value);
	}

	public void setStringArray(String fieldName, String[] value) {
		setObject(fieldName, value);
	}

	public String toString() {
		return entity.toString();
	}
}
