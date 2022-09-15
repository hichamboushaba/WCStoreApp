//
//  ProductsList.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 10/3/2022.
//

import SwiftUI
import Combine
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

class HomeViewModelProxy: ViewModelProxy<HomeViewModel> {
    @Published var productsState: ProductsUiListState = ProductsUiListState(products: [], hasNext: true, state: LoadingState.loading)
    
    override init() {
        super.init()
        assignToPublished(from:\.productsNative, to: &$productsState)
    }
}

struct HomeScreen: View {
    @StateObject private var viewModelProxy = HomeViewModelProxy()
    
    private var viewModel: HomeViewModel {
        return viewModelProxy.viewModel
    }
    
    var body: some View {
        Screen(hasNavigationBar: false) {
            ProductsList(
                productsState: viewModelProxy.productsState,
                onProductClick: viewModel.onProductClicked, loadNext: viewModel.loadNext)
        }
        .effects(viewModel: viewModel)
    }
}
