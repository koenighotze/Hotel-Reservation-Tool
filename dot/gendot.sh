#!/bin/bash

for i in `ls *dot`; do
  png_name=$(basename $i .dot)
  dot -Tpng  $i  > ${png_name}.png
done
