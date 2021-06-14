/*
 *   Copyright 2021, Petr Laštovička as Lasta apps, All rights reserved
 *
 *     This file is part of President Countdown.
 *
 *     This app is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This app is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this app.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package cz.lastaapps.president.whatsnew.assets

import android.content.Context
import kotlinx.coroutines.yield
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Loads version update info from storage
 * */
internal class Loader(private val context: Context) {

    internal suspend fun load(): List<Version> {

        val xml = BufferedReader(InputStreamReader(context.assets.open("whats_new.xml"))).readText()

        return parseXML(xml)
    }

    /**
     * parses assets/whats_new.xml into version objects
     * */
    private suspend fun parseXML(input: String): List<Version> {

        val versions = ArrayList<Version>()

        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val doc: Document = dBuilder.parse(input.encodeToByteArray().inputStream())
        doc.documentElement.normalize()

        val versionNodes: NodeList = doc.getElementsByTagName("versions").item(0).childNodes

        for (versionIndex in 0 until versionNodes.length) {
            val versionNode: Node = versionNodes.item(versionIndex)
            if (versionNode.nodeType == Node.ELEMENT_NODE) {
                versions += parseVersion(versionNode)
            }

            yield()
        }

        return versions.sorted()
    }

    /**
     * Parses version root structure
     * */
    private fun parseVersion(versionNode: Node): Version {

        val name = versionNode.attributes.getNamedItem("name").nodeValue
        val build = versionNode.attributes.getNamedItem("build").nodeValue.toLong()
        val released =
            LocalDate.parse(versionNode.attributes.getNamedItem("released").nodeValue)
        val translations = HashMap<String, String>()

        val type = versionNode.attributes.getNamedItem("type").nodeValue
        val (isTest, isBeta) =
            when (type) {
                "alpha" -> true to false
                "beta" -> false to true
                "release" -> false to false
                else -> throw IllegalArgumentException("Unknown version type: $type")
            }

        val translationNodes = versionNode.childNodes
        for (translationIndex in 0 until translationNodes.length) {
            val translationNode = translationNodes.item(translationIndex)

            if (translationNode.nodeType == Node.ELEMENT_NODE) {
                val localeName = translationNode.nodeName
                val content = loadTranslation(translationNode)

                translations[localeName] = content
            }
        }

        return Version(
            name,
            build,
            released,
            isAlpha = isTest,
            isBeta = isBeta,
            contents = translations
        )
    }

    /**
     * Loads the content to translation given
     * */
    private fun loadTranslation(translationNode: Node): String {
        var content = ""

        val textNodes = translationNode.childNodes
        for (textIndex in 0 until textNodes.length) {
            val textNode = textNodes.item(textIndex)

            if (textNode.nodeType == Node.TEXT_NODE)
                content += textNode.nodeValue
        }
        content = content.trimIndent()

        return content
    }
}