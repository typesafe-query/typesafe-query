package com.github.typesafe_query.util;

public final class Pair<T1,T2> {
	
	public final T1 _1;
	public final T2 _2;
	
	public Pair(T1 t1,T2 t2) {
		_1 = t1;
		_2 = t2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_1 == null) ? 0 : _1.hashCode());
		result = prime * result + ((_2 == null) ? 0 : _2.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (_1 == null) {
			if (other._1 != null)
				return false;
		} else if (!_1.equals(other._1))
			return false;
		if (_2 == null) {
			if (other._2 != null)
				return false;
		} else if (!_2.equals(other._2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pair [_1=" + _1 + ", _2=" + _2 + "]";
	}
}
