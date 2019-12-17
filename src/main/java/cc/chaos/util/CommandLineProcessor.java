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
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

import java.util.logging.Logger;
import java.util.logging.Level;

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
            // TODO: copy template?
            // TODO: add callback object?
            // TODO: other identifier object (e.g. command string)?
            input_      = input;
        }

        @Override
        public void run()
        {
            Process proc;
            try {
                proc = template_.start();
                // TODO: notify: started(this)
            } catch (IOException ioe) {
                LOGGER.log(Level.SEVERE, "cannot start a process", ioe);
                return;
            }

            // push input to stdin
            if (input_ != null) {
                PrintWriter stdin = new PrintWriter(proc.getOutputStream());
                stdin.println(input_);
                stdin.flush();
                stdin.close();
                // TODO: notify: writtenInput(input)
            }

            // read from stdout line-by-line
            BufferedReader stdout = new BufferedReader(new InputStreamReader(
                                            proc.getInputStream()));
            String line;
            while(true)
            {
                try {
                    line = stdout.readLine();
                } catch (IOException ioe) {
                    LOGGER.log(Level.SEVERE, "error reading output from a process", ioe);
                    break;
                }
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
                LOGGER.warning(String.format("killing a seemingly unresponding process: %s",
                                String.join(" ", template_.command())));
                proc.destroy();

                // TODO notify: killed()
            }
        }
    }
}
