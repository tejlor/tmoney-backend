#!/bin/bash

rc=0

function compile {    
	mvn clean package -Dmaven.test.skip=true
	rc=$?
}

function start {
	if [[ $rc -ne 0 ]] ; then
		exit
	fi

	java -jar target/tmoney.jar --spring.profiles.active=dev
}

function prod {
	java -jar target/tmoney.jar --spring.profiles.active=prod
}

function test {
	mvn clean package
}

while getopts "cstv:" opt; do
	case $opt in
	c)
		compile $OPTARG
		;;
	s)
		start
		;;
	t)
		test
		;;
	p)
		prod
		;;
	\?)
		echo "Dopuszczalne opcje to: c (compile), s (start), t (test), p (prod)."      
		;;
	esac
done
