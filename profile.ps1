$testFiles = Get-ChildItem -Path "./tests" -File
  
Write-Host "Test, Messy Time (s), NT Time (s)`n"

$numTests = 5

foreach ($file in $testFiles) {
  # Write-Host "Times Taken for ${file}:"
  Write-Host -NoNewline "${file}"
  $messyTimeTotal = 0
  for($i=1; $i -le $numTests; $i++){
    $messyTime = Measure-Command {
      scala ./Messy.jar -i "./tests/$file" 2> $null
      z3 out.z3
    }
    $messyTimeTotal+= $messyTime.TotalSeconds
  }
  Write-Host -NoNewline ", $($messyTimeTotal/$numTests)"
  $ntTimeTotal = 0
  for($i=1; $i -le $numTests; $i++){
    $ntTime = Measure-Command {
      scala .\target\scala-2.13\Messy-1.0-fat.jar -i "./tests/$file" --nt 2> $null
    }
    $ntTimeTotal += $ntTime.TotalSeconds
  }
  Write-Host -NoNewline ", $($ntTimeTotal/$numTests)"
  Write-Host ""
    # Write-Host ", $($messyTime.TotalSeconds)"
  # Write-Host "${file}, $($messyTime.TotalSeconds), $($ntTime.TotalSeconds)"
  # $prodTime = Measure-Command {scala .\target\scala-2.13\Messy-1.0-fat.jar -i "./tests/$file" --prod}
  # Write-Host "`tProd: $($prodTime.TotalSeconds)"
}

