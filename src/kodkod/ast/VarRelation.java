/* 
 * Kodkod -- Copyright (c) 2005-present, Emina Torlak
 * Pardinus -- Copyright (c) 2014-present, Nuno Macedo
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
package kodkod.ast;

/**
 * Temporal relation class
 * @author Eduardo Pessoa, nmm
 */

public class VarRelation extends Relation {

	public final Relation expanded;
	
    public VarRelation(String s, int i) {
        super(s, i);
        expanded = Relation.nary(s, i + 1);
	}

    public static VarRelation nary(String paramString, int paramInt) {
        return new VarRelation(paramString, paramInt);
    }

    public static VarRelation unary(String paramString) {
        return new VarRelation(paramString, 1);
    }

    public static VarRelation binary(String paramString) {
        return new VarRelation(paramString, 2);
    }

    public static VarRelation ternary(String paramString) {
        return new VarRelation(paramString, 3);
    }

}