package com.hyversal.staffip.sql;

import java.util.List;

import lombok.Getter;

public class ExecuteResult
{
	@Getter private final int numRowsChanged;
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
}
