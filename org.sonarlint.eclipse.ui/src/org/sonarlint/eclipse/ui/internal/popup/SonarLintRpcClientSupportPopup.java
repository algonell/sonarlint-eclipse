/*
 * SonarLint for Eclipse
 * Copyright (C) 2015-2024 SonarSource SA
 * sonarlint@sonarsource.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarlint.eclipse.ui.internal.popup;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.sonarlint.eclipse.core.documentation.SonarLintDocumentation;
import org.sonarlint.eclipse.ui.internal.SonarLintImages;
import org.sonarlint.eclipse.ui.internal.util.BrowserUtils;
import org.sonarlint.eclipse.ui.internal.util.PopupUtils;

/**
 *  This pop-up is displayed when the SonarLint RPC backend server exited unexpectedly. It should force the user to
 *  restart it and take the moment to investigate and to (hopefully) provide us with information about why this
 *  happened in the first place.
 */
public class SonarLintRpcClientSupportPopup extends AbstractSonarLintPopup {
  @Override
  protected String getMessage() {
    return "The RPC backend exited unexpectedly. As this should not happen, please take a look at the "
      + "troubleshooting documentation and raise an issue on the SonarLint Community Forum. For now "
      + "the only possibility to restart SonarLint is to restart the IDE :(";
  }

  @Override
  protected void createContentArea(Composite composite) {
    super.createContentArea(composite);

    addLink("Troubleshooting",
      e -> BrowserUtils.openExternalBrowser(SonarLintDocumentation.TROUBLESHOOTING_LINK, getShell().getDisplay()));
    addLink("Community Forum",
      e -> BrowserUtils.openExternalBrowser(SonarLintDocumentation.COMMUNITY_FORUM, getShell().getDisplay()));

    composite.getShell().addDisposeListener(e -> PopupUtils.removeCurrentlyDisplayedPopup(getClass()));
  }

  @Override
  protected String getPopupShellTitle() {
    return "SonarLint - RPC backend server unavailable";
  }

  @Override
  protected Image getPopupShellImage(int maximumHeight) {
    return SonarLintImages.IMG_SEVERITY_BLOCKER;
  }

  /** This way everyone calling the pop-up does not have to handle it being actually displayed or not */
  public static void displayPopupIfNotAlreadyDisplayed() {
    if (PopupUtils.popupCurrentlyDisplayed(SonarLintRpcClientSupportPopup.class)) {
      return;
    }

    PopupUtils.addCurrentlyDisplayedPopup(SonarLintRpcClientSupportPopup.class);

    var popup = new SonarLintRpcClientSupportPopup();
    popup.setFadingEnabled(false);
    popup.setDelayClose(0L);
    popup.open();
  }
}