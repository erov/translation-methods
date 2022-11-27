while (<>) {
  s/\((\N*?)\)/\(\)/g;
  print;
}
