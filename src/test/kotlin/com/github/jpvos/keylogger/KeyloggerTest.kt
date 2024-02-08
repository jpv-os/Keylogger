package com.github.jpvos.keylogger

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase

//@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class KeyloggerTest : BasePlatformTestCase() {

    fun testNothing() {
        TestCase.assertTrue(true)
    }

//    fun testXMLFile() {
//        val psiFile = myFixture.configureByText(XmlFileType.INSTANCE, "<foo>bar</foo>")
//        val xmlFile = assertInstanceOf(psiFile, XmlFile::class.java)
//
//        assertFalse(PsiErrorElementUtil.hasErrors(project, xmlFile.virtualFile))
//
//        assertNotNull(xmlFile.rootTag)
//
//        xmlFile.rootTag?.let {
//            assertEquals("foo", it.name)
//            assertEquals("bar", it.value.text)
//        }
//    }
//
//    fun testRename() {
//        myFixture.testRename("foo.xml", "foo_after.xml", "a2")
//    }
//
//    fun testProjectService() {
//        val projectService = project.service<MyProjectService>()
//
//        assertNotSame(projectService.getRandomNumber(), projectService.getRandomNumber())
//    }
//
//    override fun getTestDataPath() = "src/test/testData/rename"
}
