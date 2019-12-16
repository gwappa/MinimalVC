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

import java.io.File;
import java.io.IOException;

import cc.chaos.util.Result;
import cc.chaos.vc.Repository;
import cc.chaos.vc.RootNode;

/**
 *  a representation of a Git repository entity model in the file system.
 *  @author gwappa
 */
public class GitRepository
    extends cc.chaos.vc.AbstractRepository<GitNode>
{
    static final String BARE_REPOSITORY_DIR = ".git";

    public static Result<GitRepository> fromRoot(File rootdir) {
        try {
            return Result.success(new GitRepository(rootdir));
        } catch (IOException ioe) {
            return Result.failure(ioe.getMessage());
        }
    }

    public static Result<GitRepository> fromRoot(String rootdir) {
        return fromRoot(new File(rootdir));
    }

    /**
     *  used internally. use `fromRoot(File)` instead.
     *  @see fromRoot(File)
     */
    protected GitRepository(File rootdir)
        throws IOException
    {
        super(null);
        initialize(rootdir.getAbsoluteFile());
    }

    protected void initialize(File rootdir)
        throws IOException
    {
        if (!(new File(rootdir, BARE_REPOSITORY_DIR).isDirectory())) {
            throw new cc.chaos.vc.NotARepositoryException(rootdir);
        }
        setRootNode(new GitRootNode(this, null, rootdir));
    }
}
