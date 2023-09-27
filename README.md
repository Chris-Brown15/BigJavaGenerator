# BigJavaGenerator
# Overview
Big Java Generator (B.J.G., pronounced “big”) is a source code generation tool written in Java. It can function with any programming language, and is a text-to-text command line application.
BJG functions by searching for specifically formatted text which serve as anchor points for modification. These are called flags, and their format can be specified in any way in accordance with the language-independent  requirement.
In fact, anything can be generated, not just source code.
# How It Works
Everything in BJG works from the command line, so its behavior is based on the parameters it receives to it’s main method. A single input file is given and an output file is produced (see the documentation on commands to see more details. The input file is read in 
and searched for flags. These flags are formatted by `[prefix][long ID][suffix]`. An example will be `/*__10__*/`. And in fact, if no specific prefix and suffix are given, the prefix for flags defaults to `/*__`, and the suffix defaults to `__*/`.
For each of these flags, you can provide a task to occur on it. The tasks that can be performed are `Prepend`, `Replace`, `Remove`, `Insert`, and `Append`. Each of these takes a specific number of arguments, the types and formatting of which is explained below.
For more information, see the BJGReadMe.pdf file found in the archive of the application.
