import java.util.Locale

fun isLinuxOrMacOs(): Boolean {
    val osName = System.getProperty("os.name").toLowerCase(Locale.ROOT)
    return osName.contains("linux") || osName.contains("mac os") || osName.contains("macos")
}

val copyGitHooks by tasks.registering(Copy::class) {
    group = "git hooks"
    description = "Copies the git hooks from scripts/githooks to the .git folder."
    from("$rootDir/scripts/githooks/.") {
        include("**/*.sh")
        rename("(.*).sh", "$1")
    }
    into("$rootDir/.git/hooks")
    onlyIf { isLinuxOrMacOs() }
}

val installGitHooks by tasks.registering(Exec::class) {
    group = "git hooks"
    description = "Installs the pre-commit git hooks from scripts/githooks."
    workingDir = rootDir
    commandLine = listOf("chmod", "-R", "+x", ".git/hooks/.")
    dependsOn.add(copyGitHooks)
    onlyIf { isLinuxOrMacOs() }
    doLast {
        logger.info("Git hook installed successfully.")
    }
}

afterEvaluate {
    // We install the hook at the first occasion
    tasks["clean"].dependsOn.add(installGitHooks)
    tasks["assemble"].dependsOn.add(installGitHooks)
}
