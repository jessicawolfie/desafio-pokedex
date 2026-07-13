plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    jacoco
}

android {
    namespace = "com.example.pokedex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokedex"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Habilita a coleta de cobertura nos testes unitários do build debug.
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // --- Coroutines: base pra chamadas suspend em runtime ---
    implementation(libs.coroutines.android)

    // --- Networking ---
    implementation(libs.retrofit)
    // Converter Gson: serializa/desserializa JSON <-> DTO Kotlin
    implementation(libs.retrofit.converter.gson)
    // Logging interceptor: loga requisições no Logcat (útil pra debug)
    implementation(libs.okhttp.logging.interceptor)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.gson)

    // TESTES UNITÁRIOS (src/test — rodam na JVM, rápidos)
    // JUnit 4: runner + anotações (@Test, @Before, @After). A base.
    testImplementation(libs.junit)
    // Truth: asserts legíveis. assertThat(x).isEqualTo(y).
    testImplementation(libs.truth)
    // Mockito + extensão Kotlin: criam dublês das dependências (a Api, o Repository).
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    // Coroutines Test: TestDispatcher e runTest{} — testar coroutines sem delay real.
    testImplementation(libs.coroutines.test)
    // Arch core testing: faz LiveData/Arch Components rodarem síncronos no teste.
    testImplementation(libs.androidx.arch.core.testing)

    // TESTES INSTRUMENTADOS (src/androidTest — Espresso)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Task que gera o relatório de cobertura, excluindo boilerplate e código gerado.
tasks.register<JacocoReport>("jacocoTestReport") {
    // Só roda depois dos testes unitários de debug.
    dependsOn("testDebugUnitTest")

    reports {
        html.required.set(true)   // relatório navegável (pra você ler)
        xml.required.set(true)    // formato máquina (CI, SonarQube)
    }

    // O que NÃO medir: código gerado e dados puros sem lógica.
    val excludes = listOf(
        "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
        "**/*_Hilt*.*", "**/hilt_aggregated_deps/**",        // gerado pelo Hilt
        "**/*_Factory.*", "**/*_MembersInjector.*",          // gerado pelo Hilt
        "**/dto/**",                                         // DTOs: dados, sem lógica
        "**/databinding/**", "**/*Binding.*"                 // ViewBinding gerado
    )

    classDirectories.setFrom(
        fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
            exclude(excludes)
        }
    )
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(
        fileTree(layout.buildDirectory.get()) { include("**/*.exec", "**/*.ec") }
    )
}
