//
//  Screen.swift
//  WCStoreApp
//
//  Created by Hicham on 13/9/2022.
//

import SwiftUI
import WCStoreAppKmm

struct Screen<Content, ViewModel>: View where Content: View, ViewModel: closeable {
    private(set) var hasNavigationBar: Bool = true
    let viewModel: BaseViewModel
    
    @ViewBuilder let content: () -> Content
    
    var body: some View {
        content()
            .navigationBarHidden(!hasNavigationBar)
            .navigationBarBackButtonHidden(!hasNavigationBar)
            .setTitleIfNecessary(hasNavigationBar: hasNavigationBar)
            .onDisappear {
                viewModel.close()
            }
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
