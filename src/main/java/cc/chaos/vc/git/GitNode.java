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

package cc.chaos.vc.git;

import java.util.Enumeration;
import java.io.File;
import javax.swing.tree.TreeNode;

import cc.chaos.vc.RootNode;
import cc.chaos.vc.Repository;
import cc.chaos.vc.Node;

/**
 *  implementation for a class representing a "node" in Git.
 *  note that a default implementation is only used for non-root nodes.
 *
 *  note: there are only two ways to retrieve a child from the GitNode:
 *  either through `getChildAt()` or through `children()`.
 *
 *  for the time being, this implementation holds minimal information
 *  about the node (e.g. info about children is obtained indirectly
 *  every time the function is called). This may cost some amount of time
 *  while sparing some (but not much) memory.
 *
 *  @author gwappa
 */
public class GitNode
    implements Node<GitNode>
{
    final GitRepository repo_;

    /**
     *  holds its parent node
     */
    final GitNode   parent_;

    /**
     *  holds its corresponding file, in the absolute format.
     */
    final File      repr_;

    protected GitNode(final GitRepository repository,
                    final GitNode parent,
                    final File repr)
    {
        repo_   = repository;
        parent_ = parent;
        repr_   = repr.getAbsoluteFile();
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     *
     */
    @Override
    public boolean equals(Object other)
    {
        return (other instanceof GitNode)
                && this.repr_.equals(((GitNode)other).repr_);
    }

    /**
     *  @return the Node's name as a file.
     */
    @Override
    public String getName()
    {
        return repr_.getName();
    }

    /**
     *  @return this Node's file representation.
     */
    public File getFile()
    {
        return repr_;
    }

    @Override
    public GitNode getParentNode()
    {
        return isRoot()? null: parent_;
    }

    @Override
    public Repository<GitNode> getRepository()
    {
        return repo_;
    }

    /**
     *  @return the root Node for its registered repository.
     */
    @SuppressWarnings("unchecked")
    @Override
    public RootNode<GitNode> getRepositoryRoot()
    {
        GitNode node = this;
        while (!node.isRoot()) {
            node = node.getParentNode();
        }
        return ((RootNode<GitNode>)node);
    }

    /**
     *  @return true if this node represents the root of the repository.
     *  @see GitRootNode
     */
    @Override
    public boolean isRoot()
    {
        return false;
    }

    /**
     *  @return true if this node represents a file.
     */
    @Override
    public boolean isLeaf()
    {
        return repr_.isFile();
    }

    @Override
    public TreeNode getParent()
    {
        return getParentNode();
    }

    @Override
    public boolean getAllowsChildren()
    {
        return repr_.isDirectory();
    }

    @Override
    public int getChildCount()
    {
        if (repr_.isDirectory()) {
            return repr_.list().length;
        } else {
            return 0;
        }
    }

    @Override
    public Enumeration<GitNode> children()
    {
        if (repr_.isDirectory()) {
            return new ChildFiles(this, repr_.listFiles());

        } else {
            return new ChildFiles(this, new File[] {});
        }
    }

    @Override
    public TreeNode getChildAt(int index) {
        if (!repr_.isDirectory()) {
            return null;
        } else {
            return new GitNode(repo_, this, repr_.listFiles()[index]);
        }
    }

    @Override
    public int getIndex(TreeNode node) {
        if (!repr_.isDirectory()) {
            return -1;
        } else if (!(node instanceof GitNode)) {
            return -1;
        } else {
            GitNode nodeg = (GitNode)node;
            if (!nodeg.getParentNode().equals(this)) {
                return -1;
            }

            // otherwise check against each child
            File[] children = repr_.listFiles();
            File   target   = nodeg.getFile();
            for(int i=0; i<children.length; i++) {
                if (children[i].equals(target)) {
                    return i;
                }
            }
            // not found as its direct child
            return -1;
        }
    }

    private class ChildFiles
        extends cc.chaos.util.SimpleEnumeration<File, GitNode>
    {
        GitNode parent_;

        public ChildFiles(GitNode parent, File[] inputs){
            super(inputs);
            parent_ = parent;
        }

        protected GitNode asOutputValue(File input) {
            return new GitNode(repo_, parent_, input);
        }
    }
}
