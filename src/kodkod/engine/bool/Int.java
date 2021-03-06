/* 
 * Kodkod -- Copyright (c) 2005-2011, Emina Torlak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package kodkod.engine.bool;

import java.util.List;


/**
* An integer represented using {@link kodkod.engine.bool.BooleanValue boolean values}
* and a given {@link kodkod.engine.config.Options.IntEncoding integer encoding}.
* 
* @specfield factory: BooleanFactory
* @specfield bits: [0..factory.bitwidth) -> one factory.components
* @specfield encoding: factory.intEncoding
* @author Emina Torlak
*/
public abstract class Int {
	final BooleanFactory factory;
	
	/**
	 * Creates an Int with the given factory
	 * @ensures this.factory' = factory
	 */
	Int(BooleanFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * Throws IllegalArgumentException if other.factory != this.factory.
	 * @throws IllegalArgumentException  other.factory != this.factory.
	 */
	final void validate(Int other) {
		if (other.factory != factory)
			throw new IllegalArgumentException("other.factory != this.factory");
	}
	
	/**
	 * Returns the BooleanValue at the specified index.
	 * @requires 0 <= i < this.factory.bitwidth
	 * @return this.bits[i]
	 */
	abstract BooleanValue bit(int i);
	
	/**
	 * Returns the little endian two's complement representation of this integer that is 
	 * this.factory.bitwidth bits wide.  Specifically, the returned list L has 
	 * this.factory.bitwidth boolean values such that the meaning of this integer is 
	 * 1*[[L.get(0)]] + ... + (1<<i)*[[L.get(i)]] + ... + (-1<<(L.size()-1))*[[L.get(L.size()-1)]].
	 * @return a list containing the little endian two's complement representation of this integer.
	 * @throws UnsupportedOperationException  this integer encoding cannot be converted to two's complement.
	 */
	public abstract List<BooleanValue> twosComplementBits();
		
	/**
	 * Returns this.factory
	 * @return this.factory
	 */
	public final BooleanFactory factory() { return factory; }
	
	/**
	 * Returns the number of bits in the representation of this Int,
	 * including sign bits (if any).
	 * @return this.width
	 */
	public abstract int width(); 
	
	/**
	 * Returns true if all the bits representing this Int
	 * are BooleanConstants.
	 * @return this.bits[int] in BooleanConstant
	 */
	public abstract boolean isConstant();
	
	/**
	 * If this Int is constant, returns its value.  Otherwise
	 * throws an IllegalStateException.
	 * @return this.isConstant() => [[this.bits]]
	 * @throws IllegalStateException  !this.isConstant()
	 */
	public abstract int value();
	
	/**
	 * Returns a BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is equal to the integer represented by the specified Int.
	 * @requires this.factory = other.factory
	 * @return BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is equal to the integer represented by the specified Int
	 * @throws IllegalArgumentException  this.factory != other.factory 
	 */
	public abstract BooleanValue eq(Int other);
	
	/**
	 * Returns a BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is less than or equal to the integer
	 * represented by the specified Int
	 * @requires this.factory = other.factory 
	 * @return BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is less than or equal to the integer
	 * represented by the specified Int
	 * @throws IllegalArgumentException  this.factory != other.factory 
	 */
	public abstract BooleanValue lte(Int other);
	
	/**
	 * Returns a BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is less than the integer
	 * represented by the specified Int.
	 * @requires this.factory = other.factory
	 * @return BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is less than the integer
	 * represented by the specified Int
	 * @throws IllegalArgumentException  this.factory != other.factory 
	 */
	public abstract BooleanValue lt(Int other);
	
	/**
	 * Returns a BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is greater than or equal to the integer
	 * represented by the specified Int.
	 * @requires this.factory = other.factory 
	 * @return BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is greater than or equal to the integer
	 * represented by the specified Int
	 * @throws IllegalArgumentException  this.factory != other.factory 
	 */
	public BooleanValue gte(Int other) {
		return other.lte(this);
	}
	
	/**
	 * Returns a BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is greater than the integer
	 * represented by the specified Int.
	 * @requires this.factory = other.factory 
	 * @return BooleanValue encoding the comparator circuit
	 * that checks whether the integer represented by this
	 * Int is greater than the integer
	 * represented by the specified Int
	 * @throws IllegalArgumentException  this.factory != other.factory
	 */
	public BooleanValue gt(Int other) {
		return other.lt(this);
	}
	
	/**
	 * Returns an Int that represents the sum of this and the given Int.
	 * @requires this.factory = other.factory 
	 * @return an Int that represents the sum of this and the given Int
	 * @throws IllegalArgumentException  this.factory != other.factory
	 */
	public abstract Int plus(Int other);
	
	/**
	 * Returns an Int that represents the sum of this and the given Ints.
	 * @requires this.factory = others[int].factory 
	 * @return an Int that represents the sum of this and the given Ints
	 * @throws IllegalArgumentException  this.factory != others[int].factory
	 */
	public abstract Int plus(Int... others);
	
	/**
	 * Returns an Int that represents the product between this and the given Int.
	 * @requires this.factory = other.factory
	 * @return an Int that represents the product between this and the given Int
	 * @throws UnsupportedOperationException  this.encoding does not support multiplication
	 */
	public abstract Int multiply(Int other);
	
	/**
	 * Returns an Int that represents the product between this and the given Ints.
	 * @requires this.factory = others[int].factory
	 * @return an Int that represents the product between this and the given Ints
	 * @throws UnsupportedOperationException  this.encoding does not support multiplication
	 */
	public abstract Int multiply(Int... others);
	
	/**
	 * Returns an Int that represents the difference between this and the given Int.
	 * @requires this.factory = other.factory
	 * @return an Int that represents the difference between this and the given Int
	 * @throws UnsupportedOperationException  this.encoding does not support subtraction
	 */
	public abstract Int minus(Int other);
	
	/**
	 * Returns an Int that represents the quotient of the division between this and the given Int.
	 * @requires this.factory = other.factory
	 * @return an Int that represents the quotient of the division between this and the given Int
	 * @throws UnsupportedOperationException  this.encoding does not support division
	 */
	public abstract Int divide(Int other);
	
	/**
	 * Returns an Int that represents the remainder of the division between this and the given Int.
	 * @requires this.factory = other.factory
	 * @return an Int that represents the remainder of the division between this and the given Int
	 * @throws UnsupportedOperationException  this.encoding does not support division
	 */
	public abstract Int modulo(Int other);
	
	/**
	 * Returns an Int that evaluates to this if the condition is true, otherwise it
	 * evaluates to the given Int.
	 * @requires other + condition in this.factory.components
	 * @return an Int that evaluates to this if the condition is true, and
	 * to the given Int if the condition is false.
	 */
	public abstract Int choice(BooleanValue condition, Int other);
	
	/**
	 * Returns an Int that represents the bitwise conjunction of this and the given Int.
	 * @requires this.factory = other.factory
	 * @return an Int that represents the bitwise conjunction of this and the given Int.
	 */
	public abstract Int and(Int other);
	
	/**
	 * Returns an Int that represents the bitwise conjunction of this and the given Ints.
	 * @requires this.factory = others[int].factory
	 * @return an Int that represents the bitwise conjunction of this and the given Ints.
	 */
	public abstract Int and(Int... others);
	
	/**
	 * Returns an Int that represents the bitwise disjunction of this and the given Int.
	 * @requires this.factory = other.factory
	 * @return an Int that represents the bitwise disjunction of this and the given Int.
	 */
	public abstract Int or(Int other);
	
	/**
	 * Returns an Int that represents the bitwise disjunction of this and the given Ints.
	 * @requires this.factory = others[int].factory
	 * @return an Int that represents the bitwise disjunction of this and the given Ints.
	 */
	public abstract Int or(Int... others);
	
	/**
	 * Returns an Int that represents the bitwise XOR of this and the given Int.
	 * @requires this.factory = other.factory
	 * @return an Int that represents the bitwise XOR of this and the given Int.
	 * @throws UnsupportedOperationException  this.encoding does not support XOR
	 */
	public abstract Int xor(Int other);
	
	/**
	 * Returns an Int that represents this shifted to the left by the given Int.
	 * @requires this.factory = other.factory
	 * @return an Int that represents this shifted to the left by the given Int.
	 * @throws UnsupportedOperationException  this.encoding does not support SHL
	 */
	public abstract Int shl(Int other);
	
	/**
	 * Returns an Int that represents this shifted to the right by the given Int, 
	 * with zero extension.
	 * @requires this.factory = other.factory
	 * @return an Int that represents this shifted to the right by the given Int, 
	 * with zero extension.
	 * @throws UnsupportedOperationException  this.encoding does not support SHR
	 */
	public abstract Int shr(Int other);
	
	/**
	 * Returns an Int that represents this shifted to the right by the given Int, 
	 * with sign extension.
	 * @requires this.factory = other.factory
	 * @return an Int that represents this shifted to the right by the given Int, 
	 * with sign extension.
	 * @throws UnsupportedOperationException  this.encoding does not support SHA
	 */
	public abstract Int sha(Int other);
	
	/**
	 * Returns an Int that represents the negation of this integer.
	 * @return -[[this]]
	 * @throws UnsupportedOperationException  this.encoding does not support negation
	 */
	public abstract Int negate();
	
	/**
	 * Returns an Int that represents bitwise negation of this integer.
	 * @return ~[[this]]
	 * @throws UnsupportedOperationException  this.encoding does not support bitwise negation
	 */
	public abstract Int not();
	
	/**
	 * Returns an Int that represents the absolute value of this integer.
	 * @return abs([[this]])
	 */
	public abstract Int abs();
	
	/**
	 * Returns an Int that represents the signum of this integer.
	 * @return sgn([[this]])
	 */
	public abstract Int sgn();
	
	
}
