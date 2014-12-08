package org.koenighotze.jee7hotel.frontend.integration;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by dschmitz on 03.12.14.
 */

// Page Object for index.xhtml
@Location("index.xhtml")
public class IndexPage {

    @FindBy(tagName = "li")
    private WebElement facesMessage;

//    @FindBy(jquery = "#whoami p:visible")
  //  private WebElement signedAs;

    @FindBy(css = "input[type=submit]")
    private GrapheneElement whoAmI;
}


