while (<>) {
  s/(a(\N*?)a){3}/bad/g;
  print;
}
