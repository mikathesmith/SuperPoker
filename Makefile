run: build
	@echo Running...
	@cd src && java SuperPoker < ../test.txt
.PHONY: run

build:
	@echo Building...
	@javac src/*.java
	@echo Build complete.
.PHONY: build
