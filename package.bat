@echo off

setlocal enabledelayedexpansion

call mvnw.cmd clean package install

call mvnw.cmd -pl "client" clean assembly:assembly
call mvnw.cmd -pl "host" clean assembly:assembly

endlocal
