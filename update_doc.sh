#!/bin/sh
#
# This script is used to update site documentation.
#
# It expects that the new hupa site has been generated in $target and 
# the current deployed hupa site via svn is in $site
#
#

target=target/site
site=../site/www/hupa
pwd=`pwd`

[ ! -d $target -o ! -d $site ] && echo "Running in an invalid folder" && exit

## remove files from the old site that don't exist any more
cd $pwd/$site
for i in `find . -type f | grep -v "/\.svn/"`
do
   if [ ! -f $pwd/$target/$i ]
   then
      svn delete $i
   fi
done

add_folder() {
  n=$1
  while [ $n != . ]
  do
     [ -d $n/.svn ] && return
     [ -d $n/../.svn ] && svn add $n
     n=`dirname $n`
  done
}

## copy new files and register them in svn
cd $pwd/$target
files=`find . -type f`
cd $pwd/$site
for i in $files
do
  d=`dirname $i`
  [ ! -d $d ] && mkdir -p $d && add_folder $d
  test -f $i
  t=$?
  cp $pwd/$target/$i $d
  [ $t != 0 ] && svn add $i
done

## remove empty folders
cd $pwd/$site
for d in `find . -type d | grep -v "/\.svn" | sort -r`
do
   h=`ls $d`
   [ -z "$h" ] && svn delete $d
done

