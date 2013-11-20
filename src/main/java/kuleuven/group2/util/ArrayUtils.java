package kuleuven.group2.util;
import java.lang.reflect.Array;

/**
 * A class of array utilities.
 * 
 * @author  Group 2
 * @version	20 November 2013
 *
 */
public class ArrayUtils {

	/**
	 * Appends the two given arrays.
	 * 
	 * @param 	a
	 * 			The first array.
	 * @param 	b
	 * 			The second array.
	 * @return	The resulting array.
	 */
	public static <T> T[] concat(T[] a, T[] b) {
	   int la = a.length;
	   int lb = b.length;
	   @SuppressWarnings("unchecked")
	   T[] result = (T[]) Array.newInstance(a.getClass().getComponentType(), la + lb);
	   System.arraycopy(a, 0, result, 0, la);
	   System.arraycopy(b, 0, result, la, lb);
	   return result;
	}
}
