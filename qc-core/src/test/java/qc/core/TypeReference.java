package qc.core;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {
	private final Type _type;
	private final Class<? super T> rawType;

	@SuppressWarnings("unchecked")
	protected TypeReference() {
		Type superClass = getClass().getGenericSuperclass();
		if (superClass instanceof Class<?>) { // sanity check, should never
			// happen
			throw new IllegalArgumentException(
					"Internal error: TypeReference constructed without actual type information");
		}
		/*
		 * 22-Dec-2008, tatu: Not sure if this case is safe -- I suspect it is
		 * possible to make it fail? But let's deal with specifc case when we
		 * know an actual use case, and thereby suitable work arounds for valid
		 * case(s) and/or error to throw on invalid one(s).
		 */
		_type = ((ParameterizedType) superClass).getActualTypeArguments()[0];

		this.rawType = (Class<? super T>) getRawType(_type);
	}

	@SuppressWarnings("unchecked")
	private Class<?> getRawType(Type type) {
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType actualType = (ParameterizedType) type;
			return getRawType(actualType.getRawType());
		} else if (type instanceof GenericArrayType) {
			GenericArrayType genericArrayType = (GenericArrayType) type;
			Object rawArrayType = Array.newInstance(getRawType(genericArrayType
					.getGenericComponentType()), 0);
			return rawArrayType.getClass();
		} else if (type instanceof WildcardType) {
			WildcardType castedType = (WildcardType) type;
			return getRawType(castedType.getUpperBounds()[0]);
		} else {
			throw new IllegalArgumentException(
					"Type \'"
							+ type
							+ "\' is not a Class, "
							+ "ParameterizedType, or GenericArrayType. Can't extract class.");
		}
	}

	public Type getType() {
		return _type;
	}

	public Class<? super T> getRawType() {
		return rawType;
	}

	/**
	 * The only reason we define this method (and require implementation of
	 * <code>Comparable</code>) is to prevent constructing a reference without
	 * type information.
	 */
	public int compareTo(TypeReference<T> o) {
		// just need an implementation, not a good one... hence:
		return 0;
	}
}
