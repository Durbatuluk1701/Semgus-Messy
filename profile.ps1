$testFiles = Get-ChildItem -Path "./tests" -File

foreach ($file in $testFiles) {
  Write-Host "Times Taken for ${file}:"
  $messyTime = Measure-Command {scala ./Messy.jar -i "./tests/$file"}
  Write-Host "`tMessy: $($messyTime.TotalSeconds)"
  $ntTime = Measure-Command {scala .\target\scala-2.13\Messy-1.0-fat.jar -i "./tests/$file" --nt}
  Write-Host "`tNT: $($ntTime.TotalSeconds)"
  # $prodTime = Measure-Command {scala .\target\scala-2.13\Messy-1.0-fat.jar -i "./tests/$file" --prod}
  # Write-Host "`tProd: $($prodTime.TotalSeconds)"
}

