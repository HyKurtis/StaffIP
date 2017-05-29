package com.hyversal.staffip.sql;

import java.util.List;

public class ExecuteResult
{
	private final int numRowsChanged;
	private final List<Integer> generatedKeys;

	protected ExecuteResult(int numRowsChanged, List<Integer> generatedKeys)
	{
		this.numRowsChanged = numRowsChanged;
		this.generatedKeys = generatedKeys;
	}

	public List<Integer> getGeneratedKeys()
	{
		if (generatedKeys == null) { throw new IllegalStateException("No generated keys available"); }

		return generatedKeys;
	}
	
	public int getNumRowsChanged()
	{
		return numRowsChanged;
	}
}
