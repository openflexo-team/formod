Bxml to Text translator

INSTALL

Archive bxml2text.tar.gz contains the translator 'bxml2text' and an library directory.

Translator was compiled under Linux and tested under an Debian/Ubuntu distribution.

Set LD_LIBRARY_PATH environment variable to 'lib' directory (export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/path-to/lib).

USAGE

Execution of 'bxml2text' without arguments displays :

Usage: bxml2text [OPTION]... [FILE]
Options
 -h               : displays this help message.
 -i count         : set indentation size to count
 -O directory     : sets the output directory. If nothing is specified, the file is written to stdout.

A typical usage is

bxml2text -i 3 -O dest-dir file.bxml

If -i option is not given result file is not indented.
