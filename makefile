##############################################################################
#                                                                            #
#   Makefile for WinterWolf                                                  #
#   Copyright (c) Nick Brunt, 2010-2012                                      #
#                                                                            #
#   nickbrunt.com                                                            #
#                                                                            #
##############################################################################

# Default operation (when you only call "make")
all: winterwolf


#-----------------------------------------------------------------------------
# Compile WinterWolf:
#-----------------------------------------------------------------------------
# Javac Options:
#   sourcepath - Specifies where to look for input source files
#   d          - Specifies where to put generated class files
# Jar Options:
#   c          - create new archive
#   v          - verbose output
#   f          - archive file name
#   m          - include manifest info from specified file
#   C          - change to the specified directory and include the following file
winterwolf: clean
		#
		# Compiling WinterWolf...
		#
		javac -sourcepath src -d src src/*.java
		jar cvfm WinterWolf.jar src/manifest.txt -C src . images


#-----------------------------------------------------------------------------
# Cleaning:
#-----------------------------------------------------------------------------

# Remove all .class files and .jar file
clean:
		#
		# Removing class files and jar file...
		#
		-rm src/*.class
		-rm WinterWolf.jar