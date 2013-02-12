#!/bin/bash

## Print the usage out. Used in errors.
usage(){
cat << EOF
usage: $0 options

  This script configures the package of the android application. This is 
  neccessary because there can be only one application per package installed 
  on an Android device. 
   
  This means that if your domain is domain.com, and this application tracks 
  your inventory database, then you should make the package name 
  com.domain.inventory, and follow that pattern for additional instances to 
  avoid conflicts.

OPTIONS
  -p  Name of package. Only one application per package is allowed on Android.

EOF
}

## Make sure the name and image path were supplied
validate(){
if [[ -z $PACKAGE ]]
then
  usage
  exit 1
fi
}

PACKAGE=
while getopts "p:" OPTION
do
  case $OPTION in
    p) PACKAGE=$OPTARG;;

    \?) exit 1;;
  esac
done
validate



## Update the source code to use the new package name. These
## changes will be reflected in the final apk.
FILES=`find src/ -name "*.java" -type f`
MANIFEST=AndroidManifest.xml

for file in $FILES; do
  sed -i "s/import \(.*\)\.R;/import $PACKAGE.R;/" $file
done

sed -i "s/package=\"\(.*\)\"/package=\"$PACKAGE\"/" $MANIFEST
