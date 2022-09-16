//
//  SearchScreen.swift
//  WCStoreApp
//
//  Created by Hicham on 13/9/2022.
//

import SwiftUI
import WCStoreAppKmm

class SearchViewModelProxy: ViewModelProxy<SearchViewModel> {
    @Published var products: ProductsUiListState = ProductsUiListState(products: [], hasNext: true, state: LoadingState.loading)
    @Published var searchQuery: String!
    
    init() {
        super.init()
        assignToPublished(from:\.productsNative, to: &$products)
        searchQuery = viewModel.searchQueryNativeValue
        assignToPublished(from:\.searchQueryNative, to: &$searchQuery)
    }
}

struct SearchScreen: View {
    @StateObject private var viewModelProxy = SearchViewModelProxy()
    
    @State private var isEditing = false
    
    private var viewModel: SearchViewModel {
        return viewModelProxy.viewModel
    }
    
    var body: some View {
        Screen(hasNavigationBar: false) {
            VStack {
                SearchBar(searchText: Binding(
                    get: { viewModelProxy.searchQuery },
                    set: {
                        viewModel.onQueryChanged(query: $0)
                    }))
                ProductsList(productsState: viewModelProxy.products, onProductClick: viewModel.onProductClicked, loadNext: viewModel.loadNext)
            }
        }
    }
}

private struct SearchBar: View {
    
    @Binding var searchText: String
    
    var body: some View {
        ZStack {
            HStack {
                Image(systemName: "magnifyingglass")
                TextField("Search ..", text: $searchText)
                
                if !searchText.isEmpty {
                    Button(action: {
                        searchText = ""
                    }) {
                        Text("Cancel")
                    }
                    .padding(.trailing, 10)
                    .transition(.move(edge: .trailing))
                }
            }
            .padding(.leading, 13)
        }
        .frame(height: 40)
        .background(Color(.lightGray))
        .cornerRadius(8)
        .padding()
    }
}
