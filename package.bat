@echo off
rem
rem Copyright (c) 2023 by MULTIPLE AUTHORS
rem Part of the CS study course project.
rem

setlocal enabledelayedexpansion

call mvnw.cmd clean package install

call mvnw.cmd -pl "client" clean assembly:assembly
call mvnw.cmd -pl "host" clean assembly:assembly

endlocal
