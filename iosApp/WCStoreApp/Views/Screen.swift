//
//  Screen.swift
//  WCStoreApp
//
//  Created by Hicham on 13/9/2022.
//

import SwiftUI

struct Screen<Content>: View where Content: View {
    let hasNavigationBar: Bool
    @ViewBuilder let content: () -> Content

    var body: some View {
        content()
            .navigationBarHidden(!hasNavigationBar)
            .navigationBarBackButtonHidden(!hasNavigationBar)
            .setTitleIfNecessary(hasNavigationBar: hasNavigationBar)
    }
}

private extension View {
    @ViewBuilder func setTitleIfNecessary(hasNavigationBar: Bool) -> some View {
        if !hasNavigationBar {
            self.navigationTitle("")
        } else {
            self
        }
    }
}
