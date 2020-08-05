#! /usr/bin/env python
# encoding: utf-8
import os.path

def options(opt):
    opt.load('java')
    opt.recurse('jni')
    opt.add_option('--lib', action='store_false', default=True)

def configure(conf):
    print('→ the value of full is %r' % conf.options.lib)
    conf.load('java')
    conf.recurse('jni')

def build(bld):
    bld.recurse('jni')

    bld(rule = 'wget https://sourceforge.net/projects/javabdd/files/javabdd-win32/1.0b2%%20Win32%%20binary/${TGT}',
        target = 'javabdd_1.0b2.zip')
    bld(rule = 'yes | unzip ${SRC} -x *src.jar',
        source = 'javabdd_1.0b2.zip',
        target = 'javabdd-1.0b2.jar')
    bld.add_group()

    bld(rule = 'wget http://download.forge.ow2.org/sat4j/${TGT}',
        target = 'sat4j-core-v20130525.zip')
    bld(rule = 'unzip ${SRC} -x *src.jar',
        source = 'sat4j-core-v20130525.zip',
        target = 'org.sat4j.core.jar')
    bld(rule = 'wget http://download.forge.ow2.org/sat4j/${TGT}',
        target = 'sat4j-maxsat-v20130525.zip')
    bld(rule = 'unzip ${SRC} -x *src.jar',
        source = 'sat4j-maxsat-v20130525.zip',
        target = 'org.sat4j.maxsat.jar')
    bld(rule = 'wget http://download.forge.ow2.org/sat4j/${TGT}',
        target = 'sat4j-pb-v20130525.zip')
    bld(rule = 'unzip ${SRC} -x *src.jar',
        source = 'sat4j-pb-v20130525.zip',
        target = 'org.sat4j.pb.jar')
    bld(rule = 'wget -O slf4j-api-1.7.25.jar "http://search.maven.org/remotecontent?filepath=org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar"',
        target = 'slf4j-api-1.7.25.jar')
    bld.add_group()

    bld(features  = 'javac jar',
        name      = 'kodkod',
        srcdir    = 'src',
        outdir    = 'kodkod',
        compat    = '1.8',
        classpath = ['.', 'org.sat4j.core.jar', 'javabdd-1.0b2.jar'],
        manifest  = 'src/MANIFEST',
        basedir   = 'kodkod',
        destfile  = 'kodkod.jar')

    bld(features  = 'javac jar',
        name      = 'examples',
        use       = 'kodkod',
        srcdir    = 'examples',
        outdir    = 'examples',
        compat    = '1.8',
        classpath = ['.', 'pardinus.jar', 'org.sat4j.core.jar', 'org.sat4j.maxsat.jar', 'org.sat4j.pb.jar', 'slf4j-api-1.7.25.jar'],
        manifest  = 'src/MANIFEST',
        basedir   = 'examples',
        destfile  = 'examples.jar')

    bld.install_files('${LIBDIR}', ['kodkod.jar', 'examples.jar'])

def distclean(ctx):
    from waflib import Scripting
    Scripting.distclean(ctx)
    ctx.recurse('jni')


from waflib.Build import BuildContext
class TestContext(BuildContext):
        cmd = 'test'
        fun = 'test'

def test(bld):
    """compiles and runs tests"""

    bld(rule = 'wget -O junit.jar "http://search.maven.org/remotecontent?filepath=junit/junit/4.12/junit-4.12.jar"',
        target = 'junit.jar')
    bld(rule = 'wget -O hamcrest-core.jar "http://search.maven.org/remotecontent?filepath=org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"',
        target = 'hamcrest-core.jar')
    bld.add_group()

    cp = ['.', 'kodkod.jar', 'examples.jar', 'org.sat4j.core.jar', 'javabdd-1.0b2.jar', 'junit.jar', 'hamcrest-core.jar']
    bld(features  = 'javac',
        name      = 'test',
        srcdir    = 'test',
        classpath = cp,
        use       = ['kodkod', 'examples'])
    bld.add_group()

    bld(rule = 'java -cp {classpath} -Djava.library.path={libpath} {junit} {test}'.format(classpath = ':'.join(cp),
                                                                                          libpath = bld.env.LIBDIR,
                                                                                          junit = 'org.junit.runner.JUnitCore',
                                                                                          test = 'kodkod.test.AllTests'),
        always = True)



