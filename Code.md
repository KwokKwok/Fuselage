apply plugin: 'com.android.application'

def releaseTime() {
    return new Date().format("yyyy-MM-dd", TimeZone.getTimeZone("UTC"))
}

android {
    compileSdkVersion 21
    buildToolsVersion '21.1.2'

    defaultConfig {
        applicationId "com.boohee.*"
        minSdkVersion 14
        targetSdkVersion 21
        versionCode 1
        versionName "1.0"
        
        // dex突破65535的限制
        multiDexEnabled true
        // 默认是umeng的渠道
        manifestPlaceholders = [UMENG_CHANNEL_VALUE: "umeng"]
    }

    lintOptions {
        abortOnError false
    }

    signingConfigs {
        debug {
            // No debug config
        }

        release {
            storeFile file("../yourapp.keystore")
            storePassword "your password"
            keyAlias "your alias"
            keyPassword "your password"
        }
    }

    buildTypes {
        debug {
            // 显示Log
            buildConfigField "boolean", "LOG_DEBUG", "true"

            versionNameSuffix "-debug"
            minifyEnabled false
            zipAlignEnabled false
            shrinkResources false
            signingConfig signingConfigs.debug
        }

        release {
            // 不显示Log
            buildConfigField "boolean", "LOG_DEBUG", "false"

            minifyEnabled true
            zipAlignEnabled true
            // 移除无用的resource文件
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release

            applicationVariants.all { variant ->
                variant.outputs.each { output ->
                    def outputFile = output.outputFile
                    if (outputFile != null && outputFile.name.endsWith('.apk')) {
                    	// 输出apk名称为boohee_v1.0_2015-01-15_wandoujia.apk
                        def fileName = "boohee_v${defaultConfig.versionName}_${releaseTime()}_${variant.productFlavors[0].name}.apk"
                        output.outputFile = new File(outputFile.parent, fileName)
                    }
                }
            }
        }
    }

    // 友盟多渠道打包
    productFlavors {
        wandoujia {}
        _360 {}
        baidu {}
        xiaomi {}
        tencent {}
        taobao {}
        ...
    }

    productFlavors.all { flavor ->
        flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name]
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:support-v4:21.0.3'
    compile 'com.jakewharton:butterknife:6.0.0'
    ...
}
