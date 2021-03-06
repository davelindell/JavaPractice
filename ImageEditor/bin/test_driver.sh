cd ~/EclipseWorkspace/ImageEditor/bin
echo -e $'\nTrying: java ImageEditor'
java ImageEditor

echo $'\nTrying: java ImageEditor test'
java ImageEditor test

echo $'\nTrying: java ImageEditor audio.ppm'
java ImageEditor audio.ppm

echo $'\nTrying: java ImageEditor audio.ppm this'
java ImageEditor audio.ppm this

echo $'\nTrying: java ImageEditor audio.ppm invert'
java ImageEditor audio.ppm invert

echo $'\nTrying: java ImageEditor audio.ppm grayscale'
java ImageEditor audio.ppm grayscale

echo $'\nTrying: java ImageEditor audio.ppm emboss'
java ImageEditor audio.ppm emboss

echo $'\nTrying: java ImageEditor audio.ppm motionblur'
java ImageEditor audio.ppm motionblur

echo $'\nTrying: java ImageEditor audio.ppm motionblur 0'
java ImageEditor audio.ppm motionblur 0

echo $'\nTrying: java ImageEditor audio.ppm motionblur 25'
java ImageEditor audio.ppm motionblur 25

echo $'\nTrying: java ImageEditor audio.ppm motionblur -5'
java ImageEditor audio.ppm motionblur -5

echo $'\nTrying: java ImageEditor audio.ppm out.ppm motionblur 25'
java ImageEditor audio.ppm motionblur 25

echo $'\nTrying: java ImageEditor audio.ppm out.ppm motionblur -5'
java ImageEditor audio.ppm motionblur -5
