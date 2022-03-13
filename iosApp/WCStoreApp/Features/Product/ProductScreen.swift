//
//  ProductScreen.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 13/3/2022.
//

import SwiftUI
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

class ProductViewModelProxy: ObservableObject {
    let viewModel = KoinKt.get(objCClass: ProductViewModel.self, parameters: [43]) as! ProductViewModel
    
    @Published private(set) var uiState: ProductViewModel.UiState? = nil
    
    init() {
        createPublisher(for: viewModel.uiStateNative)
            .assertNoFailure()
            .receive(on: RunLoop.main)
            .assign(to: &$uiState)
    }
}

struct ProductScreen: View {
    @StateObject private var viewModelProxy = ProductViewModelProxy()
    
    var body: some View {
        if let state = viewModelProxy.uiState as? ProductViewModel.UiStateLoadingState {
            ProgressView()
        }
        if let state = viewModelProxy.uiState as? ProductViewModel.UiStateSuccessState {
            Text(state.product.name)
        }
    }
}

struct ProductScreen_Previews: PreviewProvider {
    static var previews: some View {
        ProductScreen()
    }
}
