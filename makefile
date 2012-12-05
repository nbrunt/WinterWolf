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
# Options:
#   make - compile to exe
#   O2   - optimise (level 2)
#   w    - hide warnings
#   o    - specify output filename
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