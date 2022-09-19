//
//  Screen.swift
//  WCStoreApp
//
//  Created by Hicham on 13/9/2022.
//

import SwiftUI
import WCStoreAppKmm

struct Screen<Content>: View where Content: View {
    private(set) var hasNavigationBar: Bool = true
    
    @ViewBuilder let content: () -> Content
    
    var body: some View {
        content()
            .navigationBarHidden(!hasNavigationBar)
    }
}
