param (
  [Parameter(Mandatory=$true)]
  [string]$path
)

$files = Get-ChildItem -Path $path -File

foreach ($file in $files) {
    $fileName = Split-Path -Path $file.FullName -Leaf

    ./semgus-parser.exe --output "./tests/$fileName.json" --format json --no-function-events --mode batch -- $file.FullName
}