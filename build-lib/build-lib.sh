mkdir rrd-static
mkdir build

export BUILD_DIR=$PWD/build
export INSTALL_DIR=$PWD/rrd-static
export PKG_CONFIG_PATH=$INSTALL_DIR/lib/pkgconfig
export PATH=$PATH:$INSTALL_DIR/bin
export CFLAGS="-fPIC"

cd $BUILD_DIR
wget https://ftp.pcre.org/pub/pcre/pcre-8.43.zip
unzip pcre-8.43.zip
cd pcre-8.43
./configure --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://ftp.acc.umu.se/pub/gnome/sources/glib/2.59/glib-2.59.0.tar.xz
tar xf glib-2.59.0.tar.xz
cd glib-2.59.0
./autogen.sh
./configure --disable-libmount --with-pcre=$INSTALL_DIR --enable-static --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://github.com/GNOME/libxml2/archive/v2.9.9.zip
unzip v2.9.9.zip
cd libxml2-2.9.9
./autogen.sh
./configure --enable-static --without-python  --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://github.com/glennrp/libpng/archive/v1.6.35.zip
unzip v1.6.35.zip
cd libpng-1.6.35
./configure --enable-static --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://download-mirror.savannah.gnu.org/releases/freetype/freetype-2.10.1.tar.xz
tar xf freetype-2.10.1.tar.xz
cd freetype-2.10.1
./configure --enable-static --with-harfbuzz=no --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://github.com/harfbuzz/harfbuzz/releases/download/2.6.4/harfbuzz-2.6.4.tar.xz
tar xf harfbuzz-2.6.4.tar.xz
cd harfbuzz-2.6.4
./configure --enable-static --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://www.cairographics.org/releases/pixman-0.38.4.tar.gz
tar xfz pixman-0.38.4.tar.gz
cd pixman-0.38.4
./configure --enable-static --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://ftp.gnu.org/pub/gnu/gperf/gperf-3.1.tar.gz
tar xfz gperf-3.1.tar.gz
cd gperf-3.1
./configure --enable-static --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://www.freedesktop.org/software/fontconfig/release/fontconfig-2.13.92.tar.gz
tar xfz fontconfig-2.13.92.tar.gz
cd fontconfig-2.13.92
./configure --enable-static --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://www.cairographics.org/releases/cairo-1.16.0.tar.xz
tar xf cairo-1.16.0.tar.xz
cd cairo-1.16.0
./configure --enable-static --disable-xlib --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://github.com/fribidi/fribidi/releases/download/v1.0.8/fribidi-1.0.8.tar.bz2
tar xf fribidi-1.0.8.tar.bz2
cd fribidi-1.0.8
./configure --enable-static --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://ftp.gnome.org/pub/GNOME/sources/pango/1.28/pango-1.28.4.tar.gz
tar xfz pango-1.28.4.tar.gz
cd pango-1.28.4
./configure --enable-static --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://github.com/libffi/libffi/releases/download/v3.3/libffi-3.3.tar.gz
tar xfz libffi-3.3.tar.gz
cd libffi-3.3
./configure --enable-static --prefix=$INSTALL_DIR
make
make install

cd $BUILD_DIR
wget https://github.com/oetiker/rrdtool-1.x/releases/download/v1.7.2/rrdtool-1.7.2.tar.gz
tar xfz rrdtool-1.7.2.tar.gz
cd rrdtool-1.7.2
./configure --disable-tcl --disable-python --disable-ruby --disable-lua --disable-rrdcgi --prefix=$INSTALL_DIR
make
make install

cp librrd4j.mri $INSTALL_DIR/lib/librrd4j.mri
cd $INSTALL_DIR/lib
ar -M <librrd4j.mri

cd $BUILD_DIR
git clone https://github.com/webfolderio/jrrd2.git
cd jrrd2/java
mvn -DskipTests package
cd ..
cd jni
mkdir build
cd build
cmake ..
strip ../dist/libjrrd2.so
