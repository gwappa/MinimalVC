/*
 * MIT License
 *
 * Copyright (c) 2019-2020 Keisuke Sehara
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cc.chaos.util;

import java.util.NoSuchElementException;

/**
 *  a utility one-time class for turning an array of elements into
 *  a certain enumerator.
 */
public class SimpleEnumeration<I, O>
    implements java.util.Enumeration<O>
{
    I[] inputs_;
    int index_;

    public SimpleEnumeration(I[] inputs)
    {
        inputs_ = inputs;
        index_  = 0;
    }

    @Override
    public boolean hasMoreElements()
    {
        return index_ < inputs_.length;
    }

    @Override
    public O nextElement()
        throws NoSuchElementException
    {
        if (index_ == inputs_.length) {
            throw new NoSuchElementException();
        } else {
            return asOutputValue(inputs_[index_++]);
        }
    }

    /**
     *  used to turn the input into an output.
     *  by default, it just casts the input class into the output class.
     */
    protected O asOutputValue(I input)
    {
        return (O)input;
    }
}
