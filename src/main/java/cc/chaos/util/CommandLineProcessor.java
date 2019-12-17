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

import java.io.File;
import java.io.BufferedReader;
import java.io.OutputStream;

import java.util.logging.Logger;

/**
 *  a class for interacting with the outputs from the command line.
 */
public class CommandLineProcessor
{
    static final Logger LOGGER = Logger.getLogger("CommandLineUtility");

    ProcessBuilder template_;

    /**
     *  initializes this Processor with the specified command.
     */
    public CommandLineProcessor(String... command)
    {
        template_ = new ProcessBuilder(command);
    }

    public void run(File directory, String input)
    {
        // TODO: add to threads list
        // and check the status from this Processor itself.
    }

    /**
     *  the class for the thread that actually runs the specified process.
     */
    private class Runner
        extends Thread
    {
        final String input_;

        public Runner(String input) {
            super();
            input_ = input;
            // TODO: read the current command (or copy the Builder object?)
        }

        @Override
        public void run()
        {
            Process proc = template_.start();

            // push input to stdin
            if (input_ != null) {
                Outputstream stdin = proc.getOutputStream();
                stdin.write(input);
                stdin.flush();
                stdin.close();
            }

            // TODO notify: started(input)

            // read from stdout line-by-line
            BufferedReader stdout = new BufferedReader(proc.getInputStream());
            String line;
            while(true)
            {
                line = stdout.readLine();
                if( line == null ){
                    break;
                }

                // TODO notify: receivedOutput(line)
                System.out.println(line);
            }

            // let the process finish
            try {
                int ret = proc.waitFor();

                // TODO: notify: finished(int)

            } catch (InterruptedException ie) {
                // TODO set command here
                LOGGER.warning("killing a seemingly unresponding process");
                proc.destroy();

                // TODO notify: killed()
            }
        }
    }
}
