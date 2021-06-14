/**
 * 
 */
package eu.cessar.ct.sdk.collections;

import java.util.BitSet;

/**
 * This class implements a matrix of bits. Each component of the bit matrix have a <code>boolean</code> value that can
 * be examined, set or cleared.
 * <p>
 * By default, all bits in the matrix initially have the value <code>false</code>.
 * <p>
 * A <code>BitMatrix</code> is not safe for multithreaded use without external synchronization.
 * 
 * @author Robi
 * 
 */
public class BitMatrix implements Cloneable
{

	private BitSet[] matrixRows;

	private int noColumns;

	private int noRows;

	/**
	 * Creates a new <code>BitMatrix</code> that is large enough to hold <code>columns x rows</code> bits. Initially all
	 * bits are set to false
	 * 
	 * @param columns
	 *        the number of columns in matrix
	 * @param rows
	 *        the number of rows in matrix
	 */
	public BitMatrix(int columns, int rows)
	{
		if (columns < 0)
		{
			throw new IllegalArgumentException("columns <= 0: " + columns); //$NON-NLS-1$
		}
		if (rows < 0)
		{
			throw new IllegalArgumentException("rows <= 0: " + rows); //$NON-NLS-1$
		}
		ensureCapacity(columns, rows);
	}

	/**
	 * Throws <code>IndexOutOfBoundsException</code> if the <code>column</code> argument is lower then zero or greater
	 * then the number of available columns.
	 * 
	 * @param column
	 */
	private void checkColumnArgument(int column)
	{
		if (column < 0 || column > noColumns)
		{
			throw new IndexOutOfBoundsException("Invalid column number: " + column); //$NON-NLS-1$
		}
	}

	/**
	 * Throws <code>IndexOutOfBoundsException</code> if the <code>row</code> argument is lower then zero or greater then
	 * the number of available rows.
	 * 
	 * @param row
	 */
	private void checkRowArgument(int row)
	{
		if (row < 0 || row > noRows)
		{
			throw new IndexOutOfBoundsException("Invalid row number: " + row); //$NON-NLS-1$
		}
	}

	/**
	 * Ensure that the matrix have enough room to hold <code>columns x rows</code> bits.
	 * 
	 * @param columns
	 *        the new number of columns
	 * @param rows
	 *        the new number of rows
	 */
	private void ensureCapacity(int columns, int rows)
	{
		noColumns = columns;
		int firstNullRowIndex = 0;
		if (matrixRows == null)
		{
			matrixRows = new BitSet[rows];
		}
		else
		{
			if (rows == matrixRows.length)
			{
				return;
			}

			firstNullRowIndex = matrixRows.length;
			if (noRows < rows)
			{
				// grow the array
				BitSet[] newMatrix = new BitSet[rows];
				System.arraycopy(matrixRows, 0, newMatrix, 0, matrixRows.length);
				matrixRows = newMatrix;
			}
			else
			{
				// shrink the array
				BitSet[] newMatrix = new BitSet[rows];
				System.arraycopy(matrixRows, 0, newMatrix, 0, rows);
				firstNullRowIndex = newMatrix.length;
				matrixRows = newMatrix;
			}
		}
		noRows = rows;
		for (int i = firstNullRowIndex; i < noRows; i++)
		{
			// by default, allocate room only for half of the column, the BitSet
			// will
			// grow as necessary
			matrixRows[i] = new BitSet(columns / 2);
		}
	}

	/**
	 * Clear all values from the matrix. After the method return all matrix bits will be set to <code>false</code>
	 */
	public void clear()
	{
		for (BitSet row: matrixRows)
		{
			row.clear();
		}
	}

	/**
	 * Set the bit at the specified position to <code>false</code>
	 * 
	 * @param column
	 *        the bit column
	 * @param row
	 *        the bit row
	 */
	public void clear(int column, int row)
	{
		checkColumnArgument(column);
		checkRowArgument(row);
		matrixRows[row].clear(column);
	}

	/**
	 * Set the bit at the specified position to <code>true</code>
	 * 
	 * @param column
	 *        the bit column
	 * @param row
	 *        the bit row
	 */
	public void set(int column, int row)
	{
		checkColumnArgument(column);
		checkRowArgument(row);
		matrixRows[row].set(column);
	}

	/**
	 * Set the bit at the specified position to <code>value</code>
	 * 
	 * @param column
	 *        the bit column
	 * @param row
	 *        the bit row
	 */
	public void set(int column, int row, boolean value)
	{
		checkColumnArgument(column);
		checkRowArgument(row);
		matrixRows[row].set(column, value);
	}

	/**
	 * Set the bit at the specified position to the complement of its current value
	 * 
	 * @param column
	 *        the bit column
	 * @param row
	 *        the bit row
	 */
	public void flip(int column, int row)
	{
		checkColumnArgument(column);
		checkRowArgument(row);
		matrixRows[row].flip(column);
	}

	/**
	 * Returnt the value of the bit at the specified position.
	 * 
	 * @param column
	 *        the bit column
	 * @param row
	 *        the bit row
	 * @return <code>true</code> if the bit is set, <code>false</code> otherwise
	 */
	public boolean get(int column, int row)
	{
		checkColumnArgument(column);
		checkRowArgument(row);
		return matrixRows[row].get(column);
	}

	/**
	 * Return the number of columns in the matrix
	 * 
	 * @return the number of columns in the <code>BitMatrix</code>
	 * 
	 */
	public int getColCount()
	{
		return noColumns;
	}

	/**
	 * Set the number of columns in the matrix. If the number of columns grows the bits from the new columns will be set
	 * to <code>false</code>
	 * 
	 * @param columns
	 *        the new number of columns of the matrix
	 */
	public void setColCount(int columns)
	{
		if (columns <= 0)
		{
			throw new IllegalArgumentException("columns <= 0: " + columns); //$NON-NLS-1$
		}
		ensureCapacity(columns, noRows);
	}

	/**
	 * Return the number of rows in the matrix
	 * 
	 * @return the number of rows in the <code>BitMatrix</code>
	 * 
	 */
	public int getRowCount()
	{
		return noRows;
	}

	/**
	 * Set the number of rows in the matrix. If the number of rows grows the bits from the new rows will be set to
	 * <code>false</code>
	 * 
	 * @param rows
	 *        the new number of rows of the matrix
	 */
	public void setRowCount(int rows)
	{
		if (rows <= 0)
		{
			throw new IllegalArgumentException("rows <= 0: " + rows); //$NON-NLS-1$
		}
		ensureCapacity(noColumns, rows);
	}

	/**
	 * Return the number of bits that have the value <code>true</code> from the specified column
	 * 
	 * @param column
	 *        the column to inspect
	 * @return the number of bits set to <code>true</code>
	 */
	public int getColSetBits(int column)
	{
		checkColumnArgument(column);
		int result = 0;
		for (BitSet row: matrixRows)
		{
			if (row.get(column))
			{
				result++;
			}
		}
		return result;
	}

	/**
	 * Return the number of bits that have the value <code>true</code> from the specified row
	 * 
	 * @param row
	 *        the row to inspect
	 * @return the number of bits set to <code>true</code>
	 */
	public int getRowSetBits(int row)
	{
		checkRowArgument(row);
		return matrixRows[row].cardinality();
	}

	/**
	 * Return the number of bits set to <code>true</code> of this <code>BitMatrix</code>
	 * 
	 * @return the cardinality of the <code>BitMatrix</code>
	 */
	public int cardinality()
	{
		int result = 0;
		for (BitSet row: matrixRows)
		{
			result += row.cardinality();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getRowCount(); i++)
		{
			for (int j = 0; j < getColCount(); j++)
			{
				sb.append(get(j, i) ? "1" : "0");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Get the specified {@code row} as a BitSet.
	 * 
	 * @param row
	 *        the row
	 * @return the BitSet that represents the row
	 */
	public BitSet getRow(int row)
	{
		checkRowArgument(row);
		return matrixRows[row];
	}

	@Override
	public BitMatrix clone()
	{
		int rowCount = getRowCount();
		BitMatrix result = new BitMatrix(getColCount(), rowCount);

		for (int i = 0; i < rowCount; i++)
		{
			result.matrixRows[i] = (BitSet) getRow(i).clone();
		}
		return result;
	}
}
