#!/bin/bash

MYDIR=`pwd`

echo "Starting in $MYDIR"

rm -rf /tmp/blog-gen

mkdir /tmp/blog-gen

git clone -b gh-pages git@github.com:daviddawson/blog.git /tmp/blog-gen

cd /tmp/blog-gen
git checkout gh-pages
cd $MYDIR
pwd

make render

rsync -av --del  --exclude=".git/" _site/ /tmp/blog-gen

cd /tmp/blog-gen

pwd
git checkout gh-pages
pwd

git add .
git status
git commit -m "Update blogsite"
git push origin -f gh-pages

cd $MYDIR
