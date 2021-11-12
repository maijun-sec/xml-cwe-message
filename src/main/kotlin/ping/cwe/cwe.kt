package ping.cwe

import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.File
import java.lang.StringBuilder
import java.util.stream.Collectors

class CweExtractor {
    var version: String? = null
    val weaknesses = mutableListOf<Weakness>()
    val relatedWeaknesses = mutableListOf<RelatedWeakness>()
    val categories = mutableListOf<Category>()
    val categoryRelationships = mutableListOf<CategoryRelationship>()
    val views = mutableListOf<View>()
    val viewMembers = mutableListOf<ViewMember>()

    constructor(filePath: String) {
        extract(filePath)
    }

    private fun extract(filePath: String) {
        val reader = SAXReader()
        val document = reader.read(File(filePath))
        val root = document.rootElement

        // cwe version
        this.version = elementAttributeValue(root, "Version")

        // Weakness
        val weaknessesElement = root.element("Weaknesses")
        extractWeaknesses(weaknessesElement)

        // Categories
        val categoriesElement = root.element("Categories")
        extractCategories(categoriesElement)

        // View
        val viewsElement = root.element("Views")
        extractViews(viewsElement)
    }

    private fun extractViews(viewsElement: Element) {
        val viewElementList = viewsElement.elements()
        for (viewElement in viewElementList) {
            val id = elementAttributeValue(viewElement, "ID")
            val name = elementAttributeValue(viewElement, "Name")
            val type = elementAttributeValue(viewElement, "Type")
            val status = elementAttributeValue(viewElement, "Status")
            val objective = elementSubElementValue(viewElement, "Objective")
            id?.let {
                val view = View(id.toInt(), name, type, status, normalize(objective), this.version!!)
                this.views.add(view)
                val membersElement = viewElement.element("Members")
                if (membersElement != null) {
                    extractViewMembers(id.toInt(), membersElement)
                }
            }
        }
    }

    private fun extractViewMembers(viewId: Int, membersElement: Element) {
        val memberElementList = membersElement.elements()
        for (memberElement in memberElementList) {
            // 当前只有 Has_Member 子节点类型
            val cweId = elementAttributeValue(memberElement, "CWE_ID")
            val viewId1 = elementAttributeValue(memberElement, "View_ID")
            val type = memberElement.name
            if (cweId != null && viewId1 != null) {
                val viewMember = ViewMember(viewId, cweId.toInt(), viewId1.toInt(), type, this.version!!)
                this.viewMembers.add(viewMember)
            }
        }
    }

    private fun extractCategories(categoriesElement: Element) {
        val categoryElementList = categoriesElement.elements()
        for (categoryElement in categoryElementList) {
            val id = elementAttributeValue(categoryElement, "ID")
            val name = elementAttributeValue(categoryElement, "Name")
            val status = elementAttributeValue(categoryElement, "Status")
            val summary = elementSubElementValue(categoryElement, "Summary")
            id?.let {
                val category = Category(id.toInt(), name, status, normalize(summary), this.version!!)
                this.categories.add(category)
                val relationships = categoryElement.element("Relationships")
                if (relationships != null) {
                    extractCategoryRelationShips(id.toInt(), relationships)
                }
            }
        }
    }

    private fun extractCategoryRelationShips(categoryId: Int, relationships: Element) {
        val relationshipElementList = relationships.elements()
        for (relationshipElement in relationshipElementList) {
            // 当前只有 Has_Member 子节点类型
            val cweId = elementAttributeValue(relationshipElement, "CWE_ID")
            val viewId = elementAttributeValue(relationshipElement, "View_ID")
            val type = relationshipElement.name
            if (cweId != null && viewId != null) {
                val categoryRelationship = CategoryRelationship(categoryId, cweId.toInt(), viewId.toInt(), type, this.version!!)
                this.categoryRelationships.add(categoryRelationship)
            }
        }
    }

    private fun extractWeaknesses(weaknessesElement: Element) {
        val weaknessElementList = weaknessesElement.elements()
        for (weaknessElement in weaknessElementList) {
            val id = elementAttributeValue(weaknessElement, "ID")
            val name = elementAttributeValue(weaknessElement, "Name")
            val abstraction = elementAttributeValue(weaknessElement, "Abstraction")
            val status = elementAttributeValue(weaknessElement, "Status")
            val description = elementSubElementValue(weaknessElement, "Description")
            val language = extractWeaknessLanguage(weaknessElement)
            id?.let {
                val weakness = Weakness(it.toInt(), name, abstraction, status, normalize(description), language, this.version!!)
                this.weaknesses.add(weakness)

                val relatedWeaknessesElement = weaknessElement.element("Related_Weaknesses")
                if (relatedWeaknessesElement != null) {
                    extractRelatedWeakness(id.toInt(), relatedWeaknessesElement)
                }
            }
        }
    }

    private fun extractWeaknessLanguage(weaknessElement: Element): String {
        val language = StringBuilder()
        val applicationPlatformElement = weaknessElement.element("Applicable_Platforms")
        if (applicationPlatformElement != null) {
            val languageElements = applicationPlatformElement.elements("Language")
            languageElements?.forEach {
                val name = elementAttributeValue(it, "Name")
                val clazz = elementAttributeValue(it, "Class")
                name?.let { language.append(name).append(",") }
                clazz?.let { language.append(clazz).append(",") }
            }
        }

        if (language.isEmpty()) {
            return language.toString()
        }
        return language.substring(0, language.length - 1)
    }

    private fun extractRelatedWeakness(weaknessId: Int, relatedWeaknessesElement: Element) {
        val relatedWeaknessElementList = relatedWeaknessesElement.elements("Related_Weakness")
        for (relatedWeaknessElement in relatedWeaknessElementList) {
            val nature = elementAttributeValue(relatedWeaknessElement, "Nature")
            val target = elementAttributeValue(relatedWeaknessElement, "CWE_ID")
            val view = elementAttributeValue(relatedWeaknessElement, "View_ID")
            val ordinal = elementAttributeValue(relatedWeaknessElement, "Ordinal")
            if (nature != null && target != null && view != null) {
                val relatedWeakness = RelatedWeakness(weaknessId, target.toInt(), nature, view.toInt(), ordinal, this.version!!)
                this.relatedWeaknesses.add(relatedWeakness)
            }
        }
    }

    private fun elementAttributeValue(element: Element, attribute: String): String? {
        val attr = element.attribute(attribute)
        if (attr != null) {
            return attr.stringValue
        }
        return null
    }

    private fun elementSubElementValue(element: Element, subElement: String): String? {
        val elm = element.element(subElement)
        if (elm != null) {
            return elm.stringValue
        }
        return null
    }

    private fun normalize(line: String?): String? {
        if (line == null) {
            return null
        }

        val lst = line.split("\r\n", "\r", "\n")
        return lst.stream().map { it.trim() }.filter { it.isNotEmpty() }.collect(Collectors.joining(" "))
    }
}