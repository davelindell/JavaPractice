cd ~/workspace/ImageEditor/bin

echo -e $'Processing Transforms...'
java ImageEditor slctemple.ppm outGrayscale.ppm grayscale
java ImageEditor slctemple.ppm outInvert.ppm invert
java ImageEditor slctemple.ppm outEmboss.ppm emboss
java ImageEditor slctemple.ppm outMotion.ppm motionblur 10


echo -e $'Compare Grayscale'
java CompareStrings outGrayscale.ppm slcGray.ppm

echo -e $'Compare Invert'
java CompareStrings outInvert.ppm slcInvert.ppm

echo -e $'Compare Emboss'
java CompareStrings outEmboss.ppm slcEmboss.ppm

echo -e $'Compare Blur'
java CompareStrings outMotion.ppm slcMotion.ppm
