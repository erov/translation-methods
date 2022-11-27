$BOF = 1;
$needEmptyStringBefore = 0;

while (<>) {
  s/\<(\N*?)\>//g;
  if (/^(\s*)$/) {
    if ($BOF eq 0) {
      $needEmptyStringBefore = 1;
    }
  } else {
    s/(\s)\s+/$1/g;
    s/^(\s+)//;
    s/(\s+)$/\n/;
    if ($needEmptyStringBefore eq 1) {
      print "\n";
      print;
      $needEmptyStringBefore = 0;
    } else {
      print;
    }
    if ($BOF eq 1) {
      $BOF = 0;
    }
  }
}

