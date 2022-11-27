while (<>) {
  s/(\b\w+\b)(\N*?)(\b\w+\b)/$3$2$1/;
  print;
}
