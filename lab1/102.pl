while (<>) {
  print if /^(\N)*\bcat\b(\N)*$/
}
