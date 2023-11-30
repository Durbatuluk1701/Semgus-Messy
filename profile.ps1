$testFiles = Get-ChildItem -Path "./tests" -File
  
Write-Host "Test, Messy Time (s), NT Time (s)`n"

foreach ($file in $testFiles) {
  # Write-Host "Times Taken for ${file}:"
  Write-Host -NoNewline "${file}"
  for($i=1; $i -le 5; $i++){
    $messyTime = Measure-Command {scala ./Messy.jar -i "./tests/$file" 2> $null}
    Write-Host -NoNewline ", $($messyTime.TotalSeconds)"
  }
  for($i=1; $i -le 5; $i++){
    $ntTime = Measure-Command {scala .\target\scala-2.13\Messy-1.0-fat.jar -i "./tests/$file" --nt 2> $null}
    Write-Host -NoNewline ", $($ntTime.TotalSeconds)"
  }
  Write-Host ""
    # Write-Host ", $($messyTime.TotalSeconds)"
  # Write-Host "${file}, $($messyTime.TotalSeconds), $($ntTime.TotalSeconds)"
  # $prodTime = Measure-Command {scala .\target\scala-2.13\Messy-1.0-fat.jar -i "./tests/$file" --prod}
  # Write-Host "`tProd: $($prodTime.TotalSeconds)"
}

