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

function version {
	cd ../tmoney-frontend
	git co dev
	npm version --no-git-tag-version ${OPTARG}
	commit_version 
	cd ../tmoney-backend
	git co dev
	mvn versions:set -DnewVersion=${OPTARG}
	commit_version
}

function commit_version {
	git add .
	git ci -m "Wersja ${OPTARG}"
	git tag ${OPTARG}
	git push
	git co master
	git merge dev
	git push --tags
	git co dev
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
	v)
		version
		;;
	\?)
		echo "Dopuszczalne opcje to: c (compile), s (start), t (test), p (prod)."      
		;;
	esac
done
