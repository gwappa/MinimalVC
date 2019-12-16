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

package cc.chaos.vc;

import java.util.LinkedList;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelListener;

/**
 *  a base implementation for a version-controlled repository model.
 *  @author gwappa
 */
public abstract class AbstractRepository<V extends Node>
    implements Repository<V>
{
    LinkedList<TreeModelListener> listeners_ = new LinkedList<>();

    RootNode<V> root_;

    public AbstractRepository(RootNode<V> root)
    {
        setRootNode(root);
    }

    @Override
    public String getName()
    {
        return getRootNode().getName();
    }

    protected void setRootNode(RootNode<V> root)
    {
        root_ = root;
    }

    @Override
    public RootNode<V> getRootNode()
    {
        return root_;
    }

    @Override
    public Object getRoot()
    {
        return getRootNode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isLeaf(Object node)
    {
        return ((V)node).isLeaf();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getChildCount(Object parent)
    {
        return ((V)parent).getChildCount();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getChild(Object parent, int index)
    {
        return ((V)parent).getChildAt(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
        return ((V)parent).getIndex((V)child);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {
        // TODO
    }

    @Override
    public void addTreeModelListener(TreeModelListener l)
    {
        listeners_.add(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l)
    {
        listeners_.remove(l);
    }
}
